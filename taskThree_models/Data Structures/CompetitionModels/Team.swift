//
//  Team.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 6/30/23.
//

public struct Team: Codable, Identifiable {
    public var id: String
    let name: String
    let members: [String]
    var progress: Double
    
    
    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case name, members, progress
    }
}
