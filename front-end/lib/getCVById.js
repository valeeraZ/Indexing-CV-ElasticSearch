export default function getCVById(id){
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    return fetch(`${process.env.uri}/api/cvs/get?id=${id}`, requestOptions)
        .then(response => {return response.json()})
        .catch(error => console.log('error', error));
}