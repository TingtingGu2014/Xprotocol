
export const isUserDetailsFetched = (state) => {
	return state.detailsFetched      
}

export const getUserDetails = (state) => {
	return state => state.details    
}
