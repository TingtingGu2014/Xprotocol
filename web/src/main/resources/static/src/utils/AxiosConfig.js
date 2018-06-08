import {SERVER_IP, SERVER_PORT} from './Constants'
import axios from 'axios'

export const axiosInstance = axios.create({
    baseURL: SERVER_IP+':'+SERVER_PORT,
    headers: {
        'X-Requested-With': 'XMLHttpRequest',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'DELETE, HEAD, GET, OPTIONS, POST, PUT',
        'Access-Control-Allow-Headers': 'Content-Type',
        'Access-Control-Max-Age': '1728000',
        'Content-Type': 'application/json',
    }
});