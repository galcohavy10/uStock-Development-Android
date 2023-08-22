//
//  SearchUser.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 5/7/23.
//

import Foundation

public struct SearchUser: Identifiable, Codable {
    public let id: String
    let username: String
    let following: Bool
    
    enum CodingKeys: String, CodingKey {
        case id = "userID"
        case username, following
    }
}
