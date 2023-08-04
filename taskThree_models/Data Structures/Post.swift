//
//  Post.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 4/21/23.
//
import Foundation


public class Post: Codable, Identifiable {
    public var id: String
    var caption: String
    var media: Media
    var user: String
    var rating: Int
    var aspects: [String]?
    var upvotes: [String]?
    var downvotes: [String]?
    var createdAt: Date
    var updatedAt: Date?
    var comments: [String]?
    var tags: [String]?
    var mentions: [String]?
    var viewCount: Int?
    
    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case caption
        case media
        case user
        case rating
        case aspects
        case upvotes
        case downvotes
        case createdAt
        case updatedAt
        case comments
        case tags
        case mentions
        case viewCount
    }
}

public class Media: Codable {
    var type: String
    var content: String?
    var url: String?
    
    enum CodingKeys: String, CodingKey {
        case type
        case content
        case url
    }
}




