//
//  Competition.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 5/15/23.
//

import Foundation

public struct Competition: Codable, Identifiable {
    public var id: String
    let name: String
    let description: String?
    let goalType: String
    let competitionGoal: String?
    let timeConstraint: Int?
    let endDate: Date?
    let participantsModel: String
    let participants: [String]
    
    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case name, description, goalType, competitionGoal, timeConstraint, endDate, participantsModel, participants
    }
}


public struct CompetitionProgressUpdate {
    let competition: String
    let team: String
    let post: String? //in case this is an update with no post
    let progress: Int? //in case this is an update with no progress
    let date: Date
}




public struct CompetitionResponse: Codable { //for matching to node api response which gives {competition: Competition object response}.
    let competition: Competition
}
