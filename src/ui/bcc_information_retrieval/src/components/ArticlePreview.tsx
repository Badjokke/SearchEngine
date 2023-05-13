import React from "react";
import "../assets/ArticlePreview.css"



const ArticlePreview = (props:{article_id:number,title:string, articleSnippet:string, author:string,})=>{
    return(
        <li className="article-item">
            <div className="article-preview-container">
                <h2 className="article-title"><a className="article-link" href={`/article?id=${props.article_id}`}>{props.title}</a></h2>
                <h3 className="article-author">{props.author || "No author"}</h3>
                <p className="article-preview">
                    {props.articleSnippet}
                </p>
            </div>
        </li>
    )
}





export default ArticlePreview;