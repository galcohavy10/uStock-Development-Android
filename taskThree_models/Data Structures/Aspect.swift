//
//  Aspect.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 4/28/23.
//

import Foundation

public struct Aspect: Codable, Identifiable, Hashable {
    public let id: String
    public let name: String
    public let description: String?
    public let admin: [String]
    public let members: [String]?
    public let posts: [String]?
    public let awards: [String]?
    public let competitions: [String]?
    public let createdAt: Date?
    public let colorScheme: ColorScheme?
    public let aspectGoal: AspectGoal?

    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case name, description, admin, members, posts, colorScheme, aspectGoal, awards, competitions, createdAt
    }

}

public struct ColorScheme: Codable, Hashable {
    public let primary: String?
    public let secondary: String?
}

public struct AspectGoal: Codable, Hashable {
    public let goal: String?
    public let progress: Double?
}


