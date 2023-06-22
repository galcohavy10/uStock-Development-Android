# uStock-Development-Android

### Finish Date: July 3rd

Members: James and Marco.

This is the -potential- product development repository for uStock on Android. You guys are gonna take on the role of converting the frontend of the iPhone app to Android. 

First, a quick overview of uStock's frontend development. We use SwiftUI as the framework for building the app, and here's an example for you. This example will serve as your first task and I will attach corresponding files in the taskOne folder.

1. JourneyView opens.
2. Upon appearing, it calls the API- (on the phone, there's a processing interface that sends data to the server).
3. It gets the reply from the server, and needs to parse it. For this, you need data structures
For Example: Posts may contain images, a caption, an array of comments, and so on. What if something is missing? The server needs to know how the data is structured, what values are optional, etc...
4. The frontend is displayed: you can keep this short and sweet, but just try and keep everything simple for now.

```swift
struct JourneyView: View {
    @State private var posts: [Post] = []
    let api = API()
    
    var body: some View {
        ScrollView {
            if posts.count > 0 {
                LazyVGrid(columns: [GridItem(.adaptive(minimum: 300))], spacing: 10) {
                    ForEach(posts.reversed().prefix(numberOfPostsToShow), id: \.id) { post in
                        PostView(post: post)
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
    //IGNORE USER ID: you don't need to worry about being able to get this quite yet. 
    //You may use Marco's userID for testing: "646d7100141bdacde51e66b6"
        api.getMyPosts(userID: defaultUserID) { result in 
            DispatchQueue.main.async { self.postsAreLoading = false }
            switch result {
            case .success(let fetchedPosts):
                DispatchQueue.main.async {
                    self.posts = fetchedPosts
                }
            case .failure(let error):
                print("Error fetching posts: \(error)")
            }
        }
    }
}
```

So, how would you set up a simple `PostView`? What's crucial here is setting up the data structure for a post correctly, which would look something like this:

```swift
public class Post: Codable {
    var id: String
    var caption: String
    var media: Media
    var user: String
    // ... (snipped for brevity)
}
```

Then you could display a simple stack of a few elements in the PostView.

In order to build the frontend, it relies on a global API class which allows making calls to the server and parsing the responses. Here's a snippet of this Swift API class as an example of the first two functions:

```swift
public let server = "https://enigmatic-plateau-21257.herokuapp.com" //this is the actual server url

public class API {
    public func makeGetRequest(endpoint: String, completion: @escaping (Result<Data, Error>) -> Void) {
        // ... (snipped for brevity)
    }
    
    func makePostRequest(endpoint: String, body: [String: Any], completion: @escaping (Result<Data, Error>) -> Void) {
        // ... (snipped for brevity)
    }
}
```

Know that setting up the API class is the backbone of getting this app to safely communicate. 

You guys can commit everything to this repository online or from your machine using github desktop. I ENCOURAGE commiting working (or broken) changes as quickly as possible to not lose your progress.

I encourage you to split the work between the two of you, to minimize confusion and redoing the same work.

##### I know this might be a difficult timeline but it's worth shooting for it. The beginning of projects is always the most painful and difficult, might as well dive in.
