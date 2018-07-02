import {axiosInstance} from './AxiosConfig'
import * as utils from '../utils/Utils.js'
import { Notify } from 'quasar'

export function getHomePage() {

    return axiosInstance.get('/home').then( (response) => {
        if(response.status === 200){
            return true;
        }
        else{
            Notify.create({message: `Cannot laod the home page.`, timeout: 3000, color: 'negative'})
            return false
        }     
    })
}