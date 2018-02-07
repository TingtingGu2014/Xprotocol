import axios from 'axios'
import { Alert, Toast } from 'quasar'

export function deleteProtocolFile(location){
    return axios({
        method: 'delete',
        url: location,
        dataType: 'json',
        headers: {'X-Requested-With': 'XMLHttpRequest'},
    })
    .then( (response) => {
        return response
    })
    .catch( (error) => {
        return error
    });
}