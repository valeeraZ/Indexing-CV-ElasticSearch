export default function postCV(file, username){
    let formData = new FormData()
    formData.append("file", file)
    formData.append("username", username)

    var requestOptions = {
        method: 'POST',
        body: formData,
        redirect: 'follow'
      };
      
      fetch(`${process.env.uri}/api/cvs`, requestOptions)
      .then(response => {return response.text()})
      .catch(error => console.log('error', error));
}