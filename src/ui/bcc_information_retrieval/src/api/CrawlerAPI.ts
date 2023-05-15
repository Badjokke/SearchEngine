//importujeme konfigurace serveru - kde na co posloucha
import apis from "../lib/global/apis";
import { ArticleListItem } from "../lib/global/datatypes";
//samotna funkce, kterou tato service vystrkuje ven - provolani endpointu, ktery vrati vsechny dokumenty obsahujici
//dotaz v promenne @param searchQuery
//funkce je asynchronni - neblokujici vykonavani kodu mezi jednotlivymi funkcemi
//asynchronni kod zajistuje vykonani jednoho bloku kodu a nasledne preda rizeni dal
//asynchronni funkce neni to stejne jako funkce spustena na novem vlakne
const crawlPage = async (searchQuery:string):Promise<{message:string}> =>{
    //fetch na endpoint serveru, v hlavicce deklarujeme, ze posilame text a metodu (v tomto priade je to GET)
    //fetch take bezi asynchronne, nasladne navrati response serveru v json formatu
    //je nutne na tomto miste take reagovat na HTTP kod odpovedi - pokud dostaneme chybu, je vhodne na to reagovat
    const res = await fetch(`${apis.crawlerAPI.url}`, {
        headers: {
            'Content-Type': 'application/json'
        },
        body:JSON.stringify({url:searchQuery}),
        method: apis.crawlerAPI.method
    }).then(data => data.json());
    return res;

}
//funkci vystrcime ven
export default crawlPage;