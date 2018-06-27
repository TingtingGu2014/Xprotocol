import {SERVER_IP, SERVER_PORT} from './Constants'
import * as utils from '../utils/Utils.js'
import axios from 'axios'
import { Notify } from 'quasar'

axios.interceptors.response.use(function (response) {
    if(response.status !== 200 && response.status !== 204){
        Notify.create({message: `This is not a 200 response: `+response.status, timeout: 3000, color: 'negative'})
    }
    else{
        console.log('current response url = '+response.request.responseURL)
        console.log(response)
    }
    return response;
}, function (error) {
    var status = error.response.status
    var message = error.response.data.message 
    Notify.create({message: `Error response:  `+status+", message: "+message, timeout: 3000, color: 'negative'})
    sessionStorage.errorMessage = message
    router.push('/errors/' + status)
//    document.location.href = '/errors/' + status
//    alert(error.response.status + " is not a good response!")
    return Promise.reject(error);
});

let axiosInstance = axios.create({
    baseURL: SERVER_IP+':'+SERVER_PORT,
    headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'DELETE, HEAD, GET, OPTIONS, POST, PUT',
        'Access-Control-Max-Age': '1728000',
    }
});

export { axiosInstance} ;