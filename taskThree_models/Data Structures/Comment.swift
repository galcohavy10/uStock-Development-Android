//
//  Comment.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 6/7/23.
//


import Foundation

public struct Comment: Codable {
    let id: String
    let author: String
    let content: String
    let post: String
    let timestamp: Date
    let upvotes: [String]
    let downvotes: [String]
    let replies: [String]
    
    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case author, content, post, timestamp, upvotes, downvotes, replies
    }
}
