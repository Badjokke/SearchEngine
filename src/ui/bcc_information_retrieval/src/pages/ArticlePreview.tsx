import React from "react";
import {useState,useEffect} from "react";


const ArticlePreview = () => {
    const [articleId,setArticleId] = useState<number>(0);
    const [article,setArticle] = useState<{title:string,author:string,id:number,content:string}>();
    useEffect(()=>{
    const search = window.location.search;
    const params = new URLSearchParams(search);
    const id = params.get('id');
    if(id == null){
        setArticleId(-1);
        return;
    }
    const articleId = parseInt(id);
    setArticleId(articleId);
    },[]);

    useEffect(() => {
      //call api
    }, [articleId])
    



        return(
            <div className="article-container">
                {(articleId == 0 || !article) && <h1>loading article...</h1>}
                {articleId == -1 && <h1>article id not provided</h1>}
                {article&&<h1>mam clanek</h1>}
            </div>
        )

}
export default ArticlePreview;