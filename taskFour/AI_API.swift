//Some notes on these parameters, the necessities, the ignores and how to use them.

//previousMessages is a necessity when making any request after a conversation has begun and it is an array of "Message" String, isUser Boolean type.
//prompt is the last message sent by the user
//givenAIChat: pass in a constant value of "heroChat" because right now only working with one bot
//givenAINumber can be zero for the same reason
//ignore aiChatID
//isPremium should be false by default but have a toggle on the view to pass in a true value (this is for a more intelligent response
//customUserPrompt is like instructions user can give to the bot to customize all further responses but you don't have to include this if u don't want to.


public func AIChat(previousMessages: [(String, Bool)], prompt: String, givenAIChat: String, givenAINumber: Int, aiChatID: String?, isPremium: Bool, customUserPrompt: String?, completion: @escaping (Result<String, Error>) -> Void) {
        let endpoint = "/api/streamAIChat"
        
        let userID = UserPreferences.shared.userID ?? "no userID"
        
        let messages = previousMessages.map { ["role": $0.1 ? "user" : "assistant", "content": $0.0] }
        var body: [String : Any] = [
            "userID" : userID,
            "prompt": prompt,
            "previousMessages": messages,
            "givenAIChat": givenAIChat,
            "givenAINumber": givenAINumber,
            "isPremium" : isPremium 
        ]
        
        if let aiChatID = aiChatID {
            body["aiChatID"] = aiChatID
        }
        
        if let customUserPrompt = customUserPrompt { //to customize the way the bot interacts every message going forward
            body["customUserPrompt"] = customUserPrompt
        }
        
        print("Sending prompt: \(prompt)")

      //Here the url is concatenating a server variable that was declared globally in James's code. CMD+F will help you find where james declared it so you can see how to use it correctly here. 
      
        let url = URL(string: "\(server)\(endpoint)")!
        var request = URLRequest(url: url)
        request.httpMethod = "POST" //Be wary of using incorrect methods
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = try! JSONSerialization.data(withJSONObject: body, options: [])

        let delegate = MyURLSessionDelegate()
        delegate.completion = completion

        let session = URLSession(configuration: .default, delegate: delegate, delegateQueue: nil)
        let task = session.dataTask(with: request)
        task.resume()
    }

