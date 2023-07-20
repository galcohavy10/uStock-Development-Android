//
//  Wallet.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 4/23/23.
//

import Foundation

public class Wallet: Codable {
    let id: String
    let balance: Double
    let transactions: [String]
    let user: String

    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case balance, transactions, user
    }
}


