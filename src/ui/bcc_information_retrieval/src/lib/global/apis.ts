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
            url : `${rootUrl}/uploadFile`,
            method: "POST"  
        },
    crawlerAPI :
        {
            url:`${rootUrl}/crawl_page`,
            method: "POST"
        },
    articleAPI:
    {
        url: `${rootUrl}/article`,
        method:"GET"
    }

}
export default Apis


