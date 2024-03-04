console.log("this is script file")


const toggleSidebar=()=>{
	
	if($('.sidebar').is(":visible")){
		//true to band karo
		
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");

		
	}else{

    //nahi hai to show kro
    
    
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
		
	}
};

const search=()=>{
	
	let query=$("#search-input").val();
	console.log(query);
	if(query=='')
	{
       $(".search-result").hide();

		
	}else{
		
		console.log(query);
		
		
		let url=`http://localhost:8281/search/${query}`;
		fetch(url)
		.then((response)=>{
			return response.json();
		})
		.then((data)=>{
			//console.log(data); 
			
			let text=`<div class='list-group'>`;
			
			data.forEach((contact)=>{
				text+=`<a href='/user/${contact.cid}/contact' class='list-group-item list-group-item-action' > ${contact.cname} </a>`
			});
			text +=`</div>`;
			
			$(".search-result").html(text);
			$(".search-result").show();

		});
	}
	
};

///first request to server to ceate a order

const paymentStart=()=>{
	
	  console.log("payment starts");
	  var amount=$("#payment_field").val();
	  console.log(amount);
	  if(amount ==""||amount== null)
	  {
		//alert("amount is required!!");  
		 swal("Failed!", "Amount is required", "error");

		return;
	  }
	  //code 
	  //we will use ajax to send request to server to create order
	  $.ajax({
			  url:'/user/create_order',
			  data:JSON.stringify({amount:amount,info:"order_request"}),
			  contentType:"application/json",
			  type:"POST",
			  dataType:"json",
			  success:function(response){
				  //invoked where success
				  console.log(response);
				  if(response.status =="created"){
					  //open payment form
					  let options={
						  key:"rzp_test_7qy3twTOdcYuMb",
						   amount:response.amount,
						  currencry:'INR',
						  name:'Smart Contact Donation',
						  description:'Donation',
image:"https://images.rawpixel.com/image_800/cHJpdmF0ZS9sci9pbWFnZXMvd2Vic2l0ZS8yMDIzLTA4L3Jhd3BpeGVsX29mZmljZV8yX3Bob3RvX29mX2FfbGlvbl9pc29sYXRlZF9vbl9jb2xvcl9iYWNrZ3JvdW5kXzJhNzgwMjM1LWRlYTgtNDMyOS04OWVjLTY3ZWMwNjcxZDhiMV8xLmpwZw.jpg",
		  order_id:response.id,
						  handler:function(response){
							  
							  console.log(response.rezorpay_payment_id);
							  console.log(response.rezorpay_order_id);
							  console.log(response.rezorpay_signature);
							  console.log('payment successfull');
							//  alert("congrats payment successfull");
							 updatePaymentOnServer(response.rezorpay_payment_id,response.rezorpay_order_id,'paid');
							
							  swal("Good job!", "payment successfull", "success");
							  
							  
						  },
						  prefill: {
								name: "",
								email:"",
								contact: ""
								},
								notes: {
								address: "Smart COntact AAkash"
								
								},
								theme: {
								color: "#3399cc"
						  },
					  }; 
					  let rzp=new Razorpay(options);
		                   rzp.on('payment.failed', function (response){
						    console.log(response.error.code);
							 console.log(response.error.description);
							 console.log(response.error.source);
							 console.log(response.error.step);
							 console.log(response.error.reason);
							 console.log(response.error.metadata.order_id);
							 console.log(response.error.metadata.payment_id);
						//	alert("Oops payment fails"); 
	                        swal("Failed!", "Oops payment fails", "error");

							});
					  rzp.open();
				  }
			  },
			  error:function(error){
				  
				  console.log(error);
				  alert("Something went wrong!!");
			  }
			  });
			  
		};
		 //
		function updatePaymentOnServer(payment_id,order_id, status)
		 {
			 $.ajax({
			   url:'/user/update_order',
					  data:JSON.stringify({payment_id:payment_id,order_id: order_id,status:status}),
					  contentType:"application/json",
					  type:"POST",
					  dataType:"json",
					  success:function(response){
						    swal("Good job!", "payment successfull", "success");
					  },
					  error:function(error){
					swal("Failed!", "Your payment is successfull, but we didi not get on server, we will contact u as soon as possible", "error");

					  },
				}); 
		}

