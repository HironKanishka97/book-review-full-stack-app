import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Review} from "../entity/review";
import {lastValueFrom} from "rxjs";
import {ServerResponse} from "../entity/ServerResponse";

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  constructor(private http: HttpClient) {
  }

  async getAllReviews(): Promise<Review[] | []> {
    try {
      const response = await lastValueFrom(
        this.http.get<ServerResponse>('http://localhost:8080/api/v1/bookreviews/getallreviews')
      );
      return response.content;
    } catch (error) {
      console.error('Error fetching reviews:', error);
      return [];
    }
  }

  async add(rev: Review): Promise<ServerResponse> {
    try {

      const response = await lastValueFrom(this.http.post<ServerResponse>('http://localhost:8080/api/v1/bookreviews/savereview', rev));
      return response;
    } catch (error) {
      console.error('Error saving review:', error);
      return new ServerResponse("500", "Server Error", []);
    }
  }

  async update(rev: Review): Promise<ServerResponse> {
    try {
      let id = rev.id;
      const response = await lastValueFrom(this.http.put<ServerResponse>('http://localhost:8080/api/v1/bookreviews/updatereview/' + id, rev));
      return response;
    } catch (error) {
      console.error('Error updating review:', error);
      return new ServerResponse("500", "Server Error", []);
    }
  }

  async delete(id: Number): Promise<ServerResponse> {
    try {
      const response = await lastValueFrom(this.http.delete<ServerResponse>('http://localhost:8080/api/v1/bookreviews/deletereview/' + id));
      return response;
    } catch (error) {
      console.error('Error deleting review:', error);
      return new ServerResponse("500", "Server Error", []);
    }
  }


}
