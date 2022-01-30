// function isAuthorized(req){
//     return req.header('Authorization') === 'Bearer test123'
// }
function isAuthorized(req){
    let token = req.header('Authorization')
    return token.length === 47 && /^Bearer [\d\w]{40}$/.test(token)
}

module.exports = (req, res, next) => {
    if (req.url.includes("/users")){
        if (req.method === 'POST' || req.method === 'PATCH' || req.method === 'PUT'){
            if (isAuthorized(req)) { // add your authorization logic here
                next()
            } else {
                res.sendStatus(401)
            }
        } else {
            next()
        }
    } else {
        next()
    }
}