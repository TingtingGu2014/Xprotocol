import axios from 'axios'

export function deleteProtocolFile(location){
    return axios({
        method: 'delete',
        url: location,
        dataType: 'json',
        headers: {'X-Requested-With': 'XMLHttpRequest'},
    })
    .then( (response) => {
        console.log(response)
        return response
    })
    .catch( (error) => {
        console.log(error.response)
        return error.response
    });
}