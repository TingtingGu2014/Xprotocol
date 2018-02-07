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
            Toast.create.positive({html: 'The file at '+location+' has been successfully deleted!', duration: 3000})
        }
        else{
            Toast.create.negative({html: "status " + status + ": cannot delete the file at "+location+"!", duration: 3000});
        }  
        return status
    })
    .catch( (error) => {
        console.log(error);
    });
}