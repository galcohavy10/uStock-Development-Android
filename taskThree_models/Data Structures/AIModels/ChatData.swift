//
//  ChatData.swift
//  uStockIOS-V2-AI
//
//  Created by Gal Cohavy on 6/28/23.
//
// This is a packaging file, packaging between views and sending to server.

import Foundation



class ChatData: ObservableObject {
    var botMessages: [String: [BotMessage]] {
        didSet {
            objectWillChange.send()
        }
    }

    init(botMessages: [String: [BotMessage]] = [:]) {
        self.botMessages = botMessages
    }
}


struct BotMessage {
    var message: String
    var flag: Bool
}

