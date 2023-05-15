export interface Menu{
    menuTitle: string;
}

export interface RouterType{
    title: string;
    path: string;
    element: JSX.Element;
}

export interface RouterRefs {
    path: string;
    name: string;
}
export enum ButtonType{
    BUTTON = "button",
    SUBMIT = "submit"
}
export interface InputType {
    type: string;
    id: string;
    placeholder: string;
    onInput: any;
    value: string;
    className: string;
}
export interface ArticleListItem{
        author: string,
        title: string,
        content: string,
        article_id:number
}
export interface RadioButtonGroupItem{
    value:string,
    name:string,
    text:string,
    checked?:string
}
export interface Article{
    author:string,
    title: string,
    content:string
}