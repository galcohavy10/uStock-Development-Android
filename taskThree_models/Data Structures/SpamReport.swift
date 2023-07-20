//
//  SpamReport.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 6/29/23.
//

import Foundation

public struct SpamReport: Codable {
    let reporter: String
    let spamContent: String
    let reportedUser: String
    let reportedPost: String
    let reportDate: Date?
    let resolutionDate: Date?
    let status: String
    
    enum CodingKeys: String, CodingKey {
        case reporter, spamContent, reportedUser, reportedPost, reportDate, resolutionDate, status
    }
}
