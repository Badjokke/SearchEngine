
import apis from "../lib/global/apis";



const requestDocuments = async (searchQuery:string) =>{
    const requestDocuments = await fetch(`${apis.searchAPI.url}?query=${searchQuery}`,{
        headers: {
            'Content-Type': 'text/html'
        },
        method:apis.searchAPI.method});
    const json = await requestDocuments.json();
    return json
}

export default requestDocuments;