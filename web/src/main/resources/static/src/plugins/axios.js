import axios from 'axios'

export default ({ Vue }) => {
    Vue.prototype.$axios = axios.interceptors.response.use(function (response) {
        if(response.status !== 200 && response.status !== 204){
            Vue.prototype.$notify.create({message: "This is not a 200 response: "+response.status+"! ", color: 'negative'})
        }
        else{
            console.log('current response url = '+response.request.responseURL)
            console.log(response)
        }
        return response;
    }, function (error) {
        var status = error.response.status
        var message = error.response.data.message
        Vue.prototype.$notify.create({message: "Error response: "+status+", message: "+message, color: 'negative'})    
        sessionStorage.errorMessage = message
        Vue.prototype.$router.push('/errors/' + status)
        return Promise.reject(error);
    });
}
