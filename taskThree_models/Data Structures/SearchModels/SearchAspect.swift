//
//  SearchAspect.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 5/7/23.
//

import Foundation

public struct SearchAspect: Identifiable, Codable {
    public let id: String
    let aspectName: String
    let aspectDescription: String
    
    enum CodingKeys: String, CodingKey {
        case id = "aspectID"
        case aspectName
        case aspectDescription
    }

}
