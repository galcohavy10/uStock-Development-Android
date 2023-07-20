//
//  AIChat.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 6/27/23.
//

import Foundation

public struct AIChat: Codable {
    public let id: String
    let creator: String
    let assistantName: String
    let messages: [Message]
    let createdAt: Date
    let updatedAt: Date
    
    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case creator
        case assistantName
        case messages
        case createdAt
        case updatedAt
    }
}


