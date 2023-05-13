//importujeme konfigurace serveru - kde na co posloucha
import apis from "../lib/global/apis";
import { ArticleListItem } from "../lib/global/datatypes";
//samotna funkce, kterou tato service vystrkuje ven - provolani endpointu, ktery vrati vsechny dokumenty obsahujici
//dotaz v promenne @param searchQuery
//funkce je asynchronni - neblokujici vykonavani kodu mezi jednotlivymi funkcemi
//asynchronni kod zajistuje vykonani jednoho bloku kodu a nasledne preda rizeni dal
//asynchronni funkce neni to stejne jako funkce spustena na novem vlakne
const requestDocuments = async (searchQuery:string, searchVector:number=1,page:number=1):Promise<{articles:ArticleListItem[],pageCount:number}> =>{
    //fetch na endpoint serveru, v hlavicce deklarujeme, ze posilame text a metodu (v tomto priade je to GET)
    //fetch take bezi asynchronne, nasladne navrati response serveru v json formatu
    //je nutne na tomto miste take reagovat na HTTP kod odpovedi - pokud dostaneme chybu, je vhodne na to reagovat
    const res = await fetch(`${apis.searchAPI.url}?query=${searchQuery}&vectorModel=${searchVector}&page=${page}`, {
        headers: {
            'Content-Type': 'text/html'
        },
        method: apis.searchAPI.method
    }).then(data => data.json());
    let response = res.articles;
    for(let i = 0; i < response.length; i++)
        response[i] = JSON.parse(response[i]);
    return {articles: response,pageCount: res.page_count };

}
//funkci vystrcime ven
export default requestDocuments;