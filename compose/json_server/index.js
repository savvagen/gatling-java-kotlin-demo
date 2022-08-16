// index.js

function generateToken(length) {
    let result = '';
    let characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let charactersLength = characters.length;
    for ( var i = 0; i < length; i++ ) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}

let randomId = (min, max) => Math.floor(Math.random() * (max - min)) + min

module.exports = () => {

    const data = {
        posts: [],
        users: [],
        comments: [],
        todos: [],
        tasks: [],
        get_token: { token: `${generateToken(40)}` }
    }

    // Create 100 users
    for (let i = 1; i < 100; i++) {
        data.users.push(
            {
                id: i,
                name: `user${i}`,
                username: `user_name${i}`,
                email: `user${i}@test.com`,
                createdAt: new Date().toISOString()
            })
    }

    // Create 100 posts
    const possibleCategories = ["test", "cats", "dogs"]
    for (let i = 1; i < 100; i++) {
        data.posts.push(
            {
                id: i,
                title: `Post ${i}`,
                subject: `Test Subject ${i}`,
                body: `Hello This is a post ${i}`,
                category: possibleCategories[Math.floor(Math.random() * possibleCategories.length)], //`cats`,
                user: data.users[i-1].id,
                comments: [ i, randomId(1, 100), randomId(1, 100) ],
                createdAt: new Date().toISOString()
            })
    }

    //Create 1000 comments
    for (let i = 1; i < 1000; i++){
        let randomUserInd = randomId(0, data.users.length-1)
        let randomPostId = randomId(1, data.posts.length)
        data.comments.push(
            {
                id: i,
                post: randomPostId,
                name: `Test comment-${i}`,
                email: data.users[randomUserInd].email,
                likes: [randomId(1, 100)],
                dislikes: [randomId(1, 100)],
                body: `Hello There. Hello to User ${data.users[randomUserInd].email}`,
                createdAt: new Date().toISOString()
            })
    }

    // Create 1000 tasks
    for (let i = 1; i < 1000; i++){
        data.tasks.push({
            id: i,
            todo: i,
            description: `Task ${i}`,
            completed: true,
            startTime: new Date().toISOString(),
            endTime: new Date().toISOString()
        })
    }

    // Create 2000 todos
    for (let i = 1; i < 2000 + 1; i++){
        data.todos.push({
            id: i,
            user: randomId(1, 100),
            title: `Task to do ${i}`,
            completed: true,
            createdAt: new Date().toISOString(),
            tasks: [
                data.tasks[randomId(0, data.tasks.length-1)]
            ]
        })
    }

    return data
}