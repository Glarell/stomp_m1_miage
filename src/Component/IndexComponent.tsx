import React, {SyntheticEvent} from "react";
import ws from "ws";
import server from '../Tmp/server';
//npm install @types/node --save
import * as http from "http";

export interface IndexComponentProperties {
}

export interface IndexComponentState {
    message_content: string
}

export default class IndexComponent extends React.Component<IndexComponentProperties, IndexComponentState> {

    constructor(props: IndexComponentProperties) {
        super(props);
        this.state = {message_content: ""}
    }

    onClickServer() {
        console.log("youpii1");
    }

    onClickClient() {
        console.log("youpii2");
    }

    onClickMessage() {
        console.log("youpii3");
    }

    onChangeMessage(event: SyntheticEvent<HTMLInputElement, Event>) {
        console.log("youpii4");
        console.log(event.currentTarget)
        this.setState({
            message_content: event.currentTarget.value
        })
    }

    render(): React.ReactNode {
        return (
            <div>
                <h1>Projet STOMP</h1>

                <div className="form-group">
                    <div className="col-md-8">
                        <button
                            id={"btn_server"}
                            name={"btn_server"}
                            className={"btn btn-success"}
                            onClick={this.onClickServer}> Créer serveur
                        </button>
                        <button
                            id={"btn_client"}
                            name={"btn_client"}
                            className={"btn btn-warning"}
                            onClick={this.onClickClient}> Créer client
                        </button>
                        <button
                            id={"btn_message"}
                            name={"btn_message"}
                            className={"btn btn-danger"}
                            onClick={this.onClickMessage}> Envoyer message
                        </button>
                        <label htmlFor={"text_message"}> Message à envoyer :</label>
                        <input
                            type={"text"}
                            value={this.state.message_content}
                            id={"text_message"}
                            name={"message"}
                            onChange={this.onChangeMessage.bind(this)}
                            size={10}
                        />
                    </div>
                </div>
            </div>
        );
    }
}