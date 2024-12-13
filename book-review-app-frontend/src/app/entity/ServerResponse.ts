import {Review} from "./review";

export class ServerResponse {

  public code !: string;
  public message !: string;
  public content!: Review[] | [];


  constructor(status: string, message: string, data: Review[] | []) {
    this.code = status;
    this.message = message;
    this.content = data;
  }
}
