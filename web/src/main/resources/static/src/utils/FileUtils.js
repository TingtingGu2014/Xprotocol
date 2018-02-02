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
        var status = response.status;
        if(status == 200 || status == "200"){              
            alert('The file at '+location+' has been successfully deleted!')
        }
        else{
            alert("status " + status + ": cannot delete the file at "+location+"!");
        }  
        return status
    })
    .catch( (error) => {
        console.log(error);
    });
}