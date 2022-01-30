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
    for (let i = 0; i < 100; i++) {
        data.users.push(
            {
                id: i+1,
                name: `user${i+1}`,
                username: `user_name${i+1}`,
                email: `user${i+1}@test.com`,
                createdAt: new Date().toISOString()
            })
    }

    // Create 100 posts
    const possibleCategories = ["test", "cats", "dogs"]
    for (let i = 0; i < 100; i++) {
        data.posts.push(
            {
                id: i+1,
                title: `Post ${i+1}`,
                subject: `Test Subject ${i+1}`,
                body: `Hello This is a post ${i+1}`,
                category: possibleCategories[Math.floor(Math.random() * possibleCategories.length)], //`cats`,
                user: data.users[i].id,
                comments: [ i+1, Math.floor(Math.random() * 100), Math.floor(Math.random() * 100) ],
                createdAt: new Date().toISOString()
            })
    }

    //Create 1000 comments
    let comId = 0;
    let n;

    for (let i = 0; i < 100; i++){
        n = 0
        while(n !== 10) {
            n++;
            comId ++;
            data.comments.push(
                {
                    id: comId,
                    post: i + 1,
                    name: `Test comment-${comId}`,
                    email: data.users[i].email,
                    likes: [Math.floor(Math.random() * 100)],
                    dislikes: [Math.floor(Math.random() * 100)],
                    body: `Hello There. Hello to User ${data.users[i].email}`,
                    createdAt: new Date().toISOString()
                })
        }
    }

    // Create 200 tasks
    for (let i = 1; i < 201; i++){
        data.tasks.push({
            id: i,
            todo: i,
            description: `Task ${i}`,
            completed: true,
            startTime: new Date().toISOString(),
            endTime: new Date().toISOString()
        })
    }

    // Create 200 todos
    let todoId = 0;
    let t;

    for (let i = 1; i < 100 + 1; i++){
        t = 1
        while(t !== 3){
            t++;
            todoId++;
            data.todos.push({
                id: todoId,
                user: data.users[t].id,
                title: `Task ${todoId}`,
                completed: true,
                createdAt: new Date().toISOString(),
                tasks: [
                    data.tasks[i]
                ]
            })
        }
    }

    return data
}