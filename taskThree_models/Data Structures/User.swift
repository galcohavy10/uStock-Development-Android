//
//  User.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 5/19/23.
//

import Foundation

public class User: Codable {
    var id: String
    var privacy: String
    var username: String
    var email: String?
    var phoneNumber: String?
    var firstName: String?
    var lastName: String?
    var dob: Date?
    var password: String? //apple and google users
    var appleUserToken: String? //apple
    var friends: [String]?
    var wallet: String?
    var investedUsers: [String]?
    var investedAspects: [String]?
    var stock: String?
    var competitions: [String]?
    var blockedUsers: [String]?
    var profilePicture: String?
    
    enum CodingKeys: String, CodingKey {
        case id = "_id"
        case privacy
        case username
        case email
        case phoneNumber
        case firstName
        case lastName
        case dob
        case password
        case appleUserToken
        case friends
        case wallet
        case investedUsers
        case investedAspects
        case stock
        case competitions
        case blockedUsers
        case profilePicture
    }
}
