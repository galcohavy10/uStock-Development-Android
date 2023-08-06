//simplified and removed images

struct ChatBubble: View {
    let text: String
    let isUser: Bool
    
    
    // Animation state
    @State private var loadingStage: Int = 0
    let loadingTexts = ["Generating", "Thinking", "This is hard"]
    
    // Timer to increase loading stage
    let timer = Timer.publish(every: 0.3, on: .main, in: .common).autoconnect()
        
    
    // Gradient
    let gradient = LinearGradient(gradient: Gradient(colors: [Color.orange, Color.green]), startPoint: .leading, endPoint: .trailing)
    
    var body: some View {
        VStack {
            HStack {
                if isUser { Spacer() }
                
                if isUser {
                    Text(text)
                        .padding(10)
                        .font(.system(size: 16))
                        .background(Color.blue.opacity(0.7))
                        .clipShape(ChatBubbleShape(isUser: isUser))
                        .foregroundColor(text == "" ? .black.opacity(0.8) : .black)  // Hide original text color if loading
                        .textSelection(.enabled)
                } else {
                    if text.isEmpty {
                        Text(generateLoadingText())
                            .padding(10)
                            .font(.system(size: 16))
                            .background(Color.white.opacity(0.9))
                            .clipShape(ChatBubbleShape(isUser: isUser))
                            .foregroundColor(text == "" ? .black.opacity(0.8) : .black)  // Hide original text color if loading
                            .shadow(color: Color.black.opacity(0.3), radius: 2, x: 2, y: 2) // Added shadow
                            .textSelection(.enabled)
                            .overlay(
                                // Only show gradient text when loading
                                Text(text == "" ? generateLoadingText() : "")
                                    .font(.system(size: 16))  // Should match the original Text font size
                                    .foregroundColor(.clear)
                                    .textSelection(.enabled)
                                    .background(AnyView(LinearGradient(gradient: Gradient(colors: [.orange, .red]), startPoint: .leading, endPoint: .trailing)))
                                    .mask(Text(text == "" ? generateLoadingText() : "")
                                        .font(.system(size: 16)))  // Should match the original Text font size
                            )
                            .onAppear {
                                loadingStage = 0
                            }
                            .onReceive(timer) { _ in
                                loadingStage = (loadingStage + 1) % 6
                            }
                    } else {
                        VStack {
                            
                            Text(text)
                                .padding(10)
                                .font(.system(size: 12))
                                .background(Color.white.opacity(0.9))
                                .clipShape(ChatBubbleShape(isUser: isUser))
                                .foregroundColor(.black)  // Hide original text color if loading
                                .shadow(color: Color.black.opacity(0.3), radius: 2, x: 2, y: 2) // Added shadow
                                .textSelection(.enabled)
                        }
                    }
                    
                    if !isUser { Spacer() }
                }
            }
            if isUser {
                Text("Sent")
                    .font(.caption2)
                    .foregroundColor(.blue)
                    .padding(.trailing, 15)
                    .padding(.top, -5)
                    .frame(maxWidth: .infinity, alignment: .trailing)
            }
        }
        .padding([.leading, .trailing], 15)
    }
    
    // Generate the loading text based on the current stage
    func generateLoadingText() -> String {
        let currentText = loadingTexts[(loadingStage / 4) % loadingTexts.count]
        let dots = String(repeating: ".", count: (loadingStage % 3) + 1)
        return currentText + dots
    }
    
}


struct ChatBubbleShape: Shape {
    var isUser: Bool = false
    
    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(roundedRect: rect,
                                byRoundingCorners: isUser ? [.topLeft, .bottomLeft, .bottomRight] : [.topRight, .bottomRight, .bottomLeft],
                                cornerRadii: CGSize(width: 20, height: 20))
        return Path(path.cgPath)
    }
    
}
