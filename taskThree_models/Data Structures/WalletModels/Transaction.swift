//
//  Transaction.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 6/7/23.
//

import Foundation

public struct Transaction: Codable, Identifiable {
    public let id: String
    let sender: String
    let recipient: String
    let amount: Double
    let stockPercentagePurchased: Double?
    let date: Date
    let type: String
    let stock: String
    
    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case sender
        case recipient
        case amount
        case stockPercentagePurchased
        case date
        case type
        case stock
    }
}
