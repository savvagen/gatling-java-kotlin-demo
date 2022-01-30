// log.js
module.exports = (req, res, next) => {
    if (req.method === 'POST' || req.method === 'PUT' || req.method === 'PATCH'){
        console.log(req.method + ' ' + req.url + ' body:' + JSON.stringify(req.body))
    }
    next()
}