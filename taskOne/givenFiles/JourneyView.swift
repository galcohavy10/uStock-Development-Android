// Boilerplate JourneyView


struct JourneyView: View {
    @State private var posts: [Post] = []
    let api = API()
    let numberOfPostsToShow = 10 // You don't need to worry about this.

    var body: some View {
        ScrollView {
            if posts.count > 0 {
                LazyVGrid(columns: [GridItem(.adaptive(minimum: 300))], spacing: 10) {
                    ForEach(posts.reversed().prefix(numberOfPostsToShow), id: \.id) { post in
                        PostView(post: post) //you need to build this separately
                            .padding(.vertical)
                            .padding(.horizontal, 5)
                    }
                }
            } else {
                VStack {
                    Text("No Posts Yet")
                        .font(.headline)
                    Text("Get Started!!!!")
                        .font(.subheadline)
                        .foregroundColor(.gray)
                }
            }
        }
        .onAppear {
            fetchPosts()
        }
    }
    
    func fetchPosts() {
      //again, you may use marco's username here: "646d7100141bdacde51e66b6" to make this work
        api.getMyPosts(userID: userID) { result in
            DispatchQueue.main.async { }
            switch result {
            case .success(let fetchedPosts):
                DispatchQueue.main.async {
                    print(fetchedPosts)
                    self.posts = fetchedPosts
                }
            case .failure(let error):
                print("Error fetching posts: \(error)")
            }
        }
    }
}
