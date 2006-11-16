function out = add_op( v, u )
    if ndims(u) == 1
        y = 0
        for i = 1:length(v)
            y = y + v(i)*u(i);
        end        
    elseif  ndims(u) == 2
        [n,nn] = size(u)     
        y = zeros(n);
        for i = 1:length(v)
            y(:) = y(:) + v(i).*u(:,i);
        end
    elseif ndims(u) == 3
        [n,nn, nnn] = size(u);
        y = zeros(n,nn,'double');        
        for i = 1:length(v)
            y(:,:) = y(:,:) + v(i).*u(:,:,i);
        end
    end
    out = y;
end