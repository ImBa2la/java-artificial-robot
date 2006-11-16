function main()
   lf = 0.6;
   lk = 0.5;
   v = [0.3 0.7];
   
   [u1 u2] = meshgrid(0.1:0.05:0.9, 0.1:0.05:0.9); 
   z1 = exp_h(lf, lk, v, cat(3, u1,u2)); 
   z2 = exp_h(0.5, 0.5, v, cat(3, u1,u2)); 
   surface(u1,u2,z1);
   surface(u1,u2,z2);
   
   
   
%    dist = @distf;
%    function d = distf(l) 
%        d = 0;
%        v1 = 0.7;
% %        for v1 = 0.2:0.1:0.8
%            for u1 = 0.2:0.1:0.8
%             for u2 = 0.2:0.1:0.8
%                 u = [u1 u2];
%                 v = [v1, 1 - v1];
%                 t = (g_p_op(LL, v, u) - exp_h(l(1), l(2), v, u));
%                 d = d + t.*t;       
%             end
%            end
% %        end
%        d = d.^0.5;       
%    end


%   for LL = 0.1:0.05:0.4
%      LL
%      l = fminsearch(dist,[0.4 0.3])   
%      
%      [u1 u2] = meshgrid(0.1:0.05:0.9, 0.1:0.05:0.9);        
%      z2 = exp_h(l(1), l(2), v, cat(3, u1,u2));  
%      z6 = g_p_op(LL, v, cat(3, u1,u2));
%      figure     
%        [C h] = contour(u1,u2,z2);
%        set(h,'ShowText','on','TextStep',get(h,'LevelStep')*2);
%        hold on;
%        [C h] = contour(u1,u2,z6);   
%        set(h,'ShowText','on','TextStep',get(h,'LevelStep')*2);
%     end

%     z1 = pow_h(lf, lk, v, cat(3, u1,u2));
%     z2 = exp_h(lf, lk, v, cat(3, u1,u2));  
%    z3 = add_op( v, cat(3, u1,u2));  
%   z4 = plin_op(lf, lk, v, cat(3, u1,u2)); 
%   z5 = lp_op(lf, lk, v, cat(3, u1,u2));
%    z6 = g_p_op(0.4, v, cat(3, u1,u2));
   
   
%     surface(u1,u2,z5);
%     surface(u1,u2,z3);
%    surface(u1,u2,z6);   
%   surface(u1,u2,z5);   
%   surface(u1,u2,z1);
%   surface(u1,u2,z2);
%    [C h] = contour(u1,u2,z2);
%    set(h,'ShowText','on','TextStep',get(h,'LevelStep')*2);
%    hold on;
%    [C h] = contour(u1,u2,z1);   
%    set(h,'ShowText','on','TextStep',get(h,'LevelStep')*2);
end