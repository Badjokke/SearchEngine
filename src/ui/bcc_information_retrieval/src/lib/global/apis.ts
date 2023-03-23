//adresa a port, pod kterym bezi Springovy server
const rootUrl = "http://localhost:8080";



const Apis = {
    searchAPI :
        {
            url:`${rootUrl}/search`,
            method:"GET"
        },
    uploadAPI:
        {
            url : `${rootUrl}/upload`,
            method: "POST"
        },
    crawlerAPI :
        {
            url:`${rootUrl}/crawler`,
            method: "POST"
        }

}
export default Apis


