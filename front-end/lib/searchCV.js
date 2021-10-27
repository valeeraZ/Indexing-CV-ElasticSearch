export default function searchCV(keyword) {
    var requestOptions = {
      method: 'GET',
      redirect: 'follow'
    };
    
    return fetch(`${process.env.uri}/api/cvs/search?keyword=${keyword}`, requestOptions)
      .then(response => {return response.json()})
      .catch(error => console.log('error', error));
  }
  