function isAuthorized(req){
    let auth = req.header("Authorization") // expected: 'Basic test:test'
    if (auth === undefined){
        return false
    } else {
        let base64String = auth.slice(6) // "dGVzdDp0ZXN0"
        // return atob(base64String) === "test:test" // decoding for newer NodeJS version
        return Buffer.from(base64String, 'base64').toString() === "test:test" // decoding for older NodeJS versions
    }
}

module.exports = (req, res, next) => {
    //console.log(req.header("Authorization"))
    if (req.url.includes("/get_token")){
        if (req.method === "GET"){
            if (isAuthorized(req)) {
                next()
            } else {
                res.sendStatus(401)
            }
        } else {
            res.sendStatus(405)
        }
    } else {
        next()
    }
}