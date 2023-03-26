import React from "react"
import {ArticleListItem, ArticleListItems} from "../lib/global/datatypes";



const List = (props:any) =>{
    //hard coded - vytahnu prvni vraceny article
    const items = props.articles.map(({title,author,content,news_category}:ArticleListItem)=>{
        return <li className={"article-item"}><a className={"article link"}> {title}</a> {author}</li>
    });

    return(
        <div className={"container"}>
            <ul className={"document-list"}>
                {items}
            </ul>
        </div>
    )


}

export default List;