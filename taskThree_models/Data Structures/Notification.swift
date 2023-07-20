//
//  Notification.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 6/19/23.
//

import Foundation

public struct Notification: Codable, Identifiable {
    public let id: String
    let recipient: String
    let sender: String
    let message: String
    let read: Bool
    let createdAt: Date

    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case recipient, sender, message, read, createdAt
    }
}
