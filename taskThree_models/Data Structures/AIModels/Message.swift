//
//  Message.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 6/30/23.
//

import Foundation

public struct Message: Codable {
    let sender: String
    let content: String
    let timestamp: Date
    
    enum CodingKeys: String, CodingKey {
        case sender
        case content
        case timestamp = "timestamp"
    }
} 
