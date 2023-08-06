//This view has been simplified but you may simplify it even more if you like as long as the core features are in the interface.
//Be creative and you should have the iphone app near you for reference whenever needed.

struct uHeroView: View {
    @State private var prompt = ""
    var isPromptInvalid: Bool {
        prompt.count >= 2000 ||
        prompt.count < 1 || generating == true
    }
    @State private var generating = false
    @State private var customUserPrompt = "" //to customize the way the bot interacts every message going forward

    @State private var isPremium = false

    @State private var showEditOptions = false
    
    @State private var imageName: String = "robotImage"

    
    @State private var botName: String
    //TODO: make sure to set selected bot in userpreferences, as well as custom instructions
    init(botName: String = "uHero") {
        _botName = State(initialValue: botName)
    }

//This data has an attribute of observedobject because of a previous issue with chat history and you can safely ignore this.

    @ObservedObject var chatData = ChatData() //see james's data structure, for how this is used to store the array of messages. You may also build your own data model in the view if you find it to be more simple.



    
    var body: some View {
            ZStack {
                NavigationView{
                    VStack {
                        
                        if showEditOptions {
                            VStack {
                                VStack {
                                    // Existing button
                                    Button(action: {
                                        showingActionSheet = true
                                    }) {
                                        VStack {
                                            HStack {
                                                Text("Change Bot")
                                                    .font(.system(size: 14))
                                                    .padding(3)
                                                
                                                Image(systemName: "chevron.down")
                                                    .resizable()
                                                    .frame(width: 10, height: 10)
                                                    .foregroundColor(.blue)
                                            }
                                            .padding(3)
                                            .frame(maxWidth: 150)
                                            .background(
                                                RoundedRectangle(cornerRadius: 8)
                                                    .stroke(Color.gray, lineWidth: 0.5)
                                                    .shadow(color: .gray, radius: 3, x: 0, y: 3)
                                            )
                                            .background(Color.white)
                                            .cornerRadius(8)
                                            .shadow(color: .gray, radius: 3, x: 0, y: 3)
                                        }
                                        .padding(5)
                                        
                                    }
                                    .actionSheet(isPresented: $showingActionSheet) {
                                        ActionSheet(title: Text("Select Bot"), buttons: actionSheetButtons())
                                    }
                                    
                                    Text("You unlock a new bot for each community you join.")
                                        .font(.caption2)
                                        .foregroundColor(.black)
                                        .padding(3)
                                    
                                    
                                    VStack {
                                        Toggle(isOn: $isPremium) {
                                            HStack {
                                                // Display appropriate system image based on isPremium state
                                                Image(systemName: isPremium ? "brain" : "hare")
                                                    .resizable()
                                                    .frame(width: 25, height: 20)
                                                    .foregroundColor(isPremium ? .orange : .blue)
                                                
                                                Text(isPremium ? "Smart" : "Fast")
                                                    .font(.system(size: 14))
                                                    .padding(3)
                                            }
                                            
                                        }
                                        .toggleStyle(SwitchToggleStyle(tint: .blue))
                                        .padding(3)
                                        .frame(maxWidth: 150)
                                        .background(
                                            RoundedRectangle(cornerRadius: 8)
                                                .stroke(Color.gray, lineWidth: 0.5)
                                                .shadow(color: .gray, radius: 3, x: 0, y: 3)
                                        )
                                        .background(Color.white)
                                        .cornerRadius(8)
                                        .shadow(color: .gray, radius: 3, x: 0, y: 3)
                                        
                                        Text("Turn on smart model for complex questions.")
                                            .font(.caption2)
                                            .foregroundColor(.black)
                                            .padding(3)
                                        
                                    }
                                    .padding(3)
                                }
                                
                                VStack {
                                    Text("GIVE YOUR BOT INSTRUCTIONS")
                                        .font(.subheadline)
                                        .foregroundColor(.black)
                                        .padding(3)
                                    TextField("Example: answer everything in 1 word.", text: $customUserPrompt)
                                        .padding(.all, 10)
                                        .background(Color.white)
                                        .cornerRadius(8)
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 8)
                                                .stroke(Color(.systemGray3), lineWidth: 1)
                                        )
                                        .padding(.horizontal, 15)
                                    
                                    Text("Turn on smart model for more steerability.")
                                        .font(.caption2)
                                        .foregroundColor(.black)
                                        .padding(3)
                                }
                                .padding(3)
                            }
                            .background(Color(red: 0.8, green: 0.8, blue: 0.8)) //light gray with 100% opacity
                        }
                        
                        
                        
                        ScrollView {
                             VStack {
                                ScrollViewReader { scrollView in
                                    VStack(alignment: .leading, spacing: 10) {
                                        if let messages = chatData.botMessages[botName] {
                                            ForEach(messages.indices, id: \.self) { index in
                                                let message = messages[index].message
                                                let isUser = messages[index].flag
                                                ChatBubble(text: message, isUser: isUser) //the chatbubble view is used to display messages in a sexy way, you can build this optionally using another file I attached for reference.
                                            }
                                        } else {
                                            VStack(alignment: .leading, spacing: 16) {
                                                ChatBubble(text: "Hi, I'm \(botName). What do you wanna know?", isUser: false)
                                            }
                                        }
                                        
                                    }
                                    .padding(.bottom, 8)
                                    .onChange(of: chatData.botMessages[botName]?.count) { _ in
                                        if let lastMessageIndex = chatData.botMessages[botName]?.indices.last {
                                            scrollView.scrollTo(lastMessageIndex, anchor: .bottom)
                                        }
                                    }
                                }
                            }
                            .gesture(
                                DragGesture()
                                    .onChanged { gesture in
                                        self.dragAmount = gesture.translation
                                        
                                        // Check if user has dragged more than 20 points
                                        if abs(self.dragAmount.width) > 15 || abs(self.dragAmount.height) > 15 {
                                            self.endEditing()
                                            showEditOptions = false
                                            
                                            // Reset dragAmount to avoid multiple calls
                                            self.dragAmount = .zero
                                        }
                                    })
                        }
                        .padding(5)
                        
                        
                        HStack {
                            GeometryReader { geometry in
                                TextField("Ask me anything...", text: $prompt, onEditingChanged: {isEditing in
                                    if isEditing {
                                        self.showDefaultPrompts = false
                                    }
                                })
                                .padding(.all, 10)
                                .background(Color.white)
                                .cornerRadius(8)
                                .overlay(
                                    RoundedRectangle(cornerRadius: 8)
                                        .stroke(Color(.systemGray3), lineWidth: 1)
                                )
                                .padding(.horizontal, 15)
                                .frame(width: geometry.size.width, height: geometry.size.height)
                            }
                            .frame(height: 50) // Adjust the height as needed

                            
                            Button(action: {
                                sendMessage()
                            }) {
                                Image(systemName: "paperplane")
                                    .font(.system(size: 24))
                                    .foregroundColor(isPromptInvalid ? Color.blue.opacity(0.7) : Color.blue)
                            }
                            .disabled(isPromptInvalid)
                            
                            
                            
                            
                        }
                        .background(Color.clear) // This makes the HStack's background transparent
                        .padding(5)
                        
                        
                    }
                    .background(Color.gray.opacity(0.1))
  
                    // FOR TITLE DESIGN
                    .navigationBarTitleDisplayMode(.inline)
                    .navigationBarItems(leading:
                        Button(action: {
                            withAnimation {
                                self.showMenu.toggle()
                            }
                        }) {
                            VStack {
                                Image(systemName: "line.horizontal.3")
                                    .imageScale(.large)
                                    .foregroundColor(.blue)
                                Text("Menu")
                                    .font(.caption2)
                                    .foregroundColor(.blue)
                            }
                        }
                    )



                    
                    .navigationBarItems(trailing:
                                            Button(action: {
                        withAnimation {
                            self.showEditOptions.toggle()
                        }
                    }) {
                        VStack {
                            Image(systemName: showEditOptions ? "x.circle.fill" : "square.and.pencil")
                                .imageScale(.large)
                                .foregroundColor(showEditOptions ? .red : .blue)
                            Text(showEditOptions ? "Hide" : "Edit")
                                .font(.caption2)
                                .foregroundColor(showEditOptions ? .red : .blue)
                        }
                        
                    }
                    )
                }
            }
        }

    }

    func sendMessage() {
        
        let currentPrompt = prompt
        prompt = ""

        generating = true
        isLoading = true


        let givenAIChat = botDetails.botName ?? ""
        let givenAINumber = botDetails.botNumber ?? 0

        // If it's the first message for this bot, add a greeting
        if chatData.botMessages[botName] == nil {
            chatData.botMessages[botName] = [BotMessage(message: "Hi, I'm \(botName). How can I assist you today?", flag: false)]
        }

        chatData.botMessages[botName]?.append(BotMessage(message: currentPrompt, flag: true)) // Add user message
        chatData.botMessages[botName]?.append(BotMessage(message: "", flag: false)) // Add empty AI message placeholder

        let previousMessagesTuples = chatData.botMessages[botName]?.map { ($0.message, $0.flag) } ?? []

        API.shared.ai.AIChat(previousMessages: previousMessagesTuples , prompt: currentPrompt, givenAIChat: givenAIChat, givenAINumber: givenAINumber, aiChatID: chatID, isPremium: isPremium, customUserPrompt: customUserPrompt) { result in
            DispatchQueue.main.async {
                print ("isPremium: \(isPremium)")
                isLoading = false

                switch result {
                case .success(let response):
                    // Separate the response into message and aiChatID
                    let components = response.components(separatedBy: "\naiChatID: ")
                    let message = components[0]
                    if components.count > 1 {
                        // Extract the aiChatID from the response
                        self.chatID = components[1]
                    }
                    
                    // If AI response is coming in, append new AI message
                    if !message.isEmpty {
                        if var lastAIMessage = chatData.botMessages[botName]?.last {
                            lastAIMessage.message += message
                            let generator = UIImpactFeedbackGenerator(style: .soft) //a haptic to indicate AI is typing
                            generator.impactOccurred()
                            if let lastMessageIndex = chatData.botMessages[botName]?.indices.last {
                                chatData.botMessages[botName]?[lastMessageIndex] = BotMessage(message: lastAIMessage.message, flag: false) // Update the last AI message
                            }
                        }
                    }
                    generating = false
                    
                case .failure(let error):
                    print("Error fetching response: \(error.localizedDescription)")
                    DispatchQueue.main.async {
                        showingAlert = true
                        self.alertMessage = "Error: \(error.localizedDescription)"
                        self.generating = false

                    }
                }
            }
        }

    }
    
 
}


