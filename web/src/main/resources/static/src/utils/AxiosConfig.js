import {SERVER_IP, SERVER_PORT} from './Constants'
import * as utils from '../utils/Utils.js'
import axios from 'axios'
import { Notify } from 'quasar'

let axiosInstance = axios.create({
    baseURL: SERVER_IP+':'+SERVER_PORT,
    headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'DELETE, HEAD, GET, OPTIONS, POST, PUT',
        'Access-Control-Max-Age': '1728000',
    }
});

axiosInstance.interceptors.request.use(function (config) {
    let xsrf = config.headers['X-XSRF-TOKEN']
    if(utils.isEmpty(xsrf)){
        config.headers['X-XSRF-TOKEN'] = localStorage.xsrf
    }
    return config;
  }, function (error) {
    return Promise.reject(error);
  });
  
axiosInstance.interceptors.response.use(function (response) {
    if(response.status !== 200 && response.status !== 204){
        if(response.status === 401){
            
            utils.clearLoggedInCookie()
                .then((data) => {
                    Notify.create({message: `You do not have authentication to see this page. Please sign in.`, color: 'warning'})
                })
                .catch((error) => {
                    console.log(error)
                })
        }
    }
    else{
        console.log('current response url = '+response.request.responseURL)
        console.log(response)
    }
    return response;
}, function (error) {
    var status = error.response.status
    if(status === 401){
            
        utils.clearLoggedInCookie()
            .then((data) => {
                Notify.create({message: `You do not have authentication to see this page. Please sign in.`, color: 'warning'})
            })
            .catch((error) => {
                console.log(error)
            })
    }
    var message = error.response.data.message 
    if(message){
        Notify.create({message: `Error response:  `+status+", message: "+message, timeout: 3000, color: 'negative'})
        sessionStorage.errorMessage = message
        router.push('/errors/' + status)
    //    document.location.href = '/errors/' + status
    //    alert(error.response.status + " is not a good response!")
        return Promise.reject(error);
    }
});

export { axiosInstance} ;