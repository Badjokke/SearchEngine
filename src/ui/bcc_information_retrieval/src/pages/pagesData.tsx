import {RouterRefs, RouterType} from "../lib/global/datatypes";
import Search from "../pages/search/Search";
import Upload from "../pages/upload/Upload";
import Crawler from "../pages/crawler/Crawler";

export const pages: RouterType[] = [
    {
        path:"",
        element:<Search/>,
        title:"Vyhledávač"
    },
    {
        path:"/upload",
        element: <Upload/>,
        title:"Nahrát soubor"
    },
    {
        path: "/crawler",
        element: <Crawler/>,
        title: "Crawler"
    }
];


export const pagesRefs: RouterRefs[] = pages.map(({path,element,title}:RouterType)=>{
    return {path:`${window.location.origin}${path}`,name:title}

});




