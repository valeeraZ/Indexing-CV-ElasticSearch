export default function getAllCVs() {
  var requestOptions = {
    method: 'GET',
    redirect: 'follow'
  };
  
  return fetch(`${process.env.uri}/api/cvs`, requestOptions)
    .then(response => {return response.json()})
    .catch(error => console.log('error', error));
}
