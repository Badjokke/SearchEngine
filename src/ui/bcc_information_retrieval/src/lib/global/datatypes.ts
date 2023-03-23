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

export interface InputType {
    type:string;
    id: string;
    placeholder:string;
    onInput:any;
    value:string;
    className:string;
}