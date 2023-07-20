//
//  Stock.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 4/23/23.
//

import Foundation

public class Stock: Codable {
    let symbol: String
    let wallet: String
    let id: String
    let history: [StockDataPoint]

    enum CodingKeys: String, CodingKey {
        case symbol
        case wallet
        case id = "_id"
        case history
    }
    

    required public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        symbol = try container.decode(String.self, forKey: .symbol)
        wallet = try container.decode(String.self, forKey: .wallet)
        id = try container.decode(String.self, forKey: .id)
        history = try container.decode([StockDataPoint].self, forKey: .history)
    }
}

public struct StockDataPoint: Codable {
    let date: Date
    let value: Double

    enum CodingKeys: String, CodingKey {
        case date
        case value
    }

    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)

        // Custom decoding for date
        let dateString = try container.decode(String.self, forKey: .date)
        let formatter = ISO8601DateFormatter()
        formatter.formatOptions = [.withInternetDateTime, .withFractionalSeconds]
        if let date = formatter.date(from: dateString) {
            self.date = date
        } else {
            throw DecodingError.dataCorruptedError(forKey: .date, in: container, debugDescription: "Date string does not match format")
        }

        // Custom decoding logic to handle both Int and Double values
        if let intValue = try? container.decode(Int.self, forKey: .value) {
            value = Double(intValue)
        } else {
            value = try container.decode(Double.self, forKey: .value)
        }
    }
}





