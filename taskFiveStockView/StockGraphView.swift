//
//  StockGraphView.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 4/26/23.
//
import SwiftUI

enum TimeRange: String, CaseIterable {
    case fiveDays = "5D", oneWeek = "1W", oneMonth = "1M", oneYear = "1Y", all = "ALL"
}

struct StockGraphView: View {
    @State var stock: Stock
    @State var isCurrentUser: Bool = false
    @State var timeRange: TimeRange = .oneMonth

    let paddingX: CGFloat = 30
    let paddingY: CGFloat = 20
    
    @State var isBouncing = false //animation
    @State private var stockIsLoading = false
    @State private var isCheckMark = false // Add this to track checkmark animation

    
    @State var isShowingTradeStockView = false

    var filteredHistory: [StockDataPoint] {
        let now = Date()
        let calendar = Calendar.current
        let endDate: Date
        switch timeRange {
        case .fiveDays:
            endDate = calendar.date(byAdding: .day, value: -5, to: now) ?? now
        case .oneWeek:
            endDate = calendar.date(byAdding: .weekOfYear, value: -1, to: now) ?? now
        case .oneMonth:
            endDate = calendar.date(byAdding: .month, value: -1, to: now) ?? now
        case .oneYear:
            endDate = calendar.date(byAdding: .year, value: -1, to: now) ?? now
        case .all:
            endDate = calendar.date(byAdding: .year, value: -100, to: now) ?? now
        }

        let filtered = stock.history.filter { $0.date >= endDate }

        // If there's no data point for the selected time range, show all history
        return filtered.isEmpty ? stock.history : filtered
    }
    
    // Define color based on stock changes
     var stockColor: Color {
         guard let lastValue = filteredHistory.last?.value,
               let firstValue = filteredHistory.first?.value else {
             return .gray
         }
         return lastValue > firstValue ? .green : .red
     }
    
    @State private var isBlinking = false
    @State private var isRedDotVisible = true
    @State private var virtualStockValue: Double? = nil // This will hold the
    @State private var virtualPointsTimer: Timer? = nil
    @State private var redDotBlinkTimer: Timer? = nil
    @State private var reloadStockTimer: Timer? = nil



    var body: some View {
        GeometryReader { geometry in
            VStack {
                HStack {
                    Text(stock.symbol + " is at ɄɄ\(String(format: "%.2f", virtualStockValue ?? filteredHistory.last?.value ?? 0.00))")
                        .font(.headline)
                        .foregroundColor(.white)
                        .padding(.bottom, 20)
                        .scaleEffect(isBouncing ? 1.1 : 1.0)
                        .animation(.interpolatingSpring(mass: 1.0, stiffness: 100.0, damping: 10, initialVelocity: 0), value: isBouncing)
                        .onBecomingVisible {
                            withAnimation(.interpolatingSpring(mass: 1.0, stiffness: 100.0, damping: 10, initialVelocity: 0)) {
                                self.isBouncing.toggle()
                            }
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
                                withAnimation(.interpolatingSpring(mass: 1.0, stiffness: 100.0, damping: 10, initialVelocity: 0)) {
                                    self.isBouncing.toggle()
                                }
                            }
                        }
                    
                    if isCurrentUser {
                        Button(action: {
                            stockIsLoading = true
                            reloadStock()
                        }) {
                            if isCheckMark {
                                Image(systemName: "checkmark") // Green checkmark
                                    .font(.headline)
                                    .foregroundColor(.green)
                                    .opacity(stockIsLoading ? 1 : 0)
                                    .animation(.easeIn(duration: 0.5), value: stockIsLoading)
                            } else {
                                Image(systemName: "arrow.clockwise") // Regular arrow
                                    .font(.headline)
                                    .foregroundColor(.blue)
                                    .opacity(stockIsLoading ? 0 : 1)
                                    .animation(.easeOut(duration: 0.5), value: stockIsLoading)
                            }

                        }
                        .padding(.bottom, 20)
                        .onChange(of: stockIsLoading) { newValue in
                            if newValue {
                                // Animate to checkmark
                                withAnimation { isCheckMark = true }
                                
                                // Delay before reverting back
                                DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
                                    withAnimation { isCheckMark = false }
                                }

                                // Delay before fading out
                                DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
                                    stockIsLoading = false
                                }
                            }
                        }
                    }
                    
                    Image(systemName: "circle.fill")
                        .foregroundColor(.red)
                        .opacity(isRedDotVisible ? 1 : 0)
                        .animation(Animation.easeInOut(duration: 1).repeatForever(autoreverses: true), value: isRedDotVisible)
                        .padding(.bottom, 20)

                }
                .padding(5)

                    ZStack {
                        // Y-axis label
                        
                        if filteredHistory.isEmpty {
                            Text("No graph yet!")
                                .font(.headline)
                                .foregroundColor(.white)
                            
                        } else if filteredHistory.count == 1 {
                            // Show the single point
                            let point = generatePoints(dataPoints: filteredHistory, width: geometry.size.width - 2 * paddingX, height: geometry.size.height - 2 * paddingY).first ?? .zero
                            Circle()
                                .fill(Color.blue)
                                .frame(width: 10, height: 10)
                                .position(point)
                                .offset(x: paddingX, y: -paddingY) // Add padding to the point
                        }
                        else {
                        Text("Value")
                            .font(.caption)
                            .rotationEffect(.degrees(-90))
                            .offset(x: -geometry.size.width / 2 + paddingX / 2, y: -paddingY)
                        
                        
                        Path { path in
                            let width = geometry.size.width - 2 * paddingX
                            let height = (geometry.size.height - 2 * paddingY) * 0.9 //decrease height a bit tp fit in constraints
                            
                            let points = generatePoints(dataPoints: filteredHistory, width: width, height: height)
                            if let firstPoint = points.first {
                                path.move(to: firstPoint)
                                
                                for point in points {
                                    path.addLine(to: point)
                                }
                            }
                        }
                        .stroke(stockColor, lineWidth: 2)  // Use stockColor for the line
                        .shadow(color: .white, radius: 0.001, x: 0.001, y: 0.001)  // Add lighter shadow for visibility
                        .offset(x: paddingX, y: -paddingY) // Add padding to the graph
                    }
                }
                    .padding(.bottom, 20) // Add padding to push the graph up

                VStack {
                    HStack {
                        Spacer()
                        Button(action: { isShowingTradeStockView = true }) {
                            Text("Trade 🤝")
                                .font(.subheadline)
                                .padding(5)
                                .background(AnyView(LinearGradient(gradient: Gradient(colors: [.orange, .red]), startPoint: .leading, endPoint: .trailing)))
                                .foregroundColor(.white)
                                .cornerRadius(20)
                        }
                    }
                    .padding(.horizontal, 20)
                    
                    HStack {
                        ForEach(TimeRange.allCases, id: \.self) { range in
                            Button(action: { self.timeRange = range }) {
                                Text(range.rawValue)
                                    .font(.caption) // smaller buttons
                            }
                            .frame(minWidth: 0, maxWidth: .infinity)
                            .padding(.vertical, 4)
                            .background(timeRange == range ? .blue : .gray)
                            .foregroundColor(.white)
                            .cornerRadius(5)
                        }
                    }
                    .padding(.horizontal, 20)
                }
            }
        }
        .onAppear {
            virtualPointsTimer?.invalidate() // Invalidate any existing timers
            redDotBlinkTimer?.invalidate()
            reloadStockTimer?.invalidate()
            
            // Blinking effect for the red dot
            redDotBlinkTimer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { _ in
                isRedDotVisible.toggle()
            }


            // Reload the stock every 20 seconds
            reloadStockTimer = Timer.scheduledTimer(withTimeInterval: 30, repeats: true) { _ in
                if isCurrentUser {
                    reloadStock()
                }
            }
            
            // This block sets up the virtual stock updates timer
            virtualPointsTimer = Timer.scheduledTimer(withTimeInterval: 2.5, repeats: true) { _ in
                if let lastValue = filteredHistory.last?.value {
                    // Randomly changing the stock value by up to 1.5
                    withAnimation(.easeInOut(duration: 1.5)) {
                        virtualStockValue = lastValue + Double.random(in: 0..<1.5)
                    }
                }
            }
        }
        
        .onDisappear {
            // Invalidate the timer when the view disappears
            virtualPointsTimer?.invalidate()
        }
        
        .fullScreenCover(isPresented: $isShowingTradeStockView) {
            TradeStockView(stock: stock)
                .modifier(DismissButton())
        }
        
    }

    func generatePoints(dataPoints: [StockDataPoint], width: CGFloat, height: CGFloat) -> [CGPoint] {
        var points: [CGPoint] = []

        let count = dataPoints.count

        if count > 1 {
            let stepX = width / CGFloat(count - 1)

            let minValue = dataPoints.min(by: { $0.value < $1.value })?.value ?? 0
            let maxValue = dataPoints.max(by: { $0.value < $1.value })?.value ?? 0
            let valueRange = maxValue - minValue

            for (index, dataPoint) in dataPoints.enumerated() {
                let x = CGFloat(index) * stepX
                let y = valueRange != 0 ? (CGFloat(dataPoint.value) - CGFloat(minValue)) / CGFloat(valueRange) * height : 0

                points.append(CGPoint(x: x, y: height - y))
            }
        }

        return points
    }

    
    func reloadStock() {
        stockIsLoading = true
        let walletID = UserPreferences.shared.walletID ?? "no id"
        API.shared.wallet.getStock(walletID: walletID) { result in
            switch result {
            case .success(let fetchedStock):
                DispatchQueue.main.async {
                    withAnimation{
                        self.stock = fetchedStock
                    }
                    stockIsLoading = false
                }
            case .failure(let error):
                print("Error fetching stock: \(error)")
            }
        }
    }
}
